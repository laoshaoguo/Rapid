package com.haohaishengshi.haohaimusic.modules.music_fm.media_data;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.AsyncTask;

import com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper;
import com.zhiyicx.common.utils.log.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE;
import static com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper.MEDIA_ID_ROOT;
import static com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper.createMediaID;


/**
 * @Author Jliuer
 * @Date 2017/2/18/14:21
 * @Email Jliuer@aliyun.com
 * @Description 音乐获取管理类
 */
public class MusicProvider {

    private MusicProviderSource mSource;

    private ConcurrentMap<String, List<MediaMetadata>> mMusicListByGenre;

    private final ConcurrentMap<String, MutableMediaMetadata> mMusicListById;

    enum State {
        NON_INITIALIZED, INITIALIZING, INITIALIZED
    }

    private volatile State mCurrentState = State.NON_INITIALIZED;

    public interface Callback {
        void onMusicCatalogReady(boolean success);
    }

    public MusicProvider() {
        this(new MusicDataConvert(null));
    }

    public MusicProvider(MusicProviderSource source) {
        mSource = source;
        mMusicListByGenre = new ConcurrentHashMap<>();
        mMusicListById = new ConcurrentHashMap<>();
    }

    public Iterable<String> getGenres() {
        if (mCurrentState != State.INITIALIZED) {
            return Collections.emptyList();
        }
        return mMusicListByGenre.keySet();
    }

    public Iterable<MediaMetadata> getShuffledMusic() {
        if (mCurrentState != State.INITIALIZED) {
            return Collections.emptyList();
        }
        List<MediaMetadata> shuffled = new ArrayList<>(mMusicListById.size());
        for (MutableMediaMetadata mutableMetadata : mMusicListById.values()) {
            shuffled.add(mutableMetadata.metadata);
        }
        Collections.shuffle(shuffled);
        return shuffled;
    }

    public Iterable<MediaMetadata> getMusicsByGenre(String genre) {
        if (mCurrentState != State.INITIALIZED || !mMusicListByGenre.containsKey(genre)) {
            return Collections.emptyList();
        }
        return mMusicListByGenre.get(genre);
    }

    public Iterable<MediaMetadata> searchMusicBySongTitle(String query) {
        return searchMusic(MediaMetadata.METADATA_KEY_TITLE, query);
    }

    public Iterable<MediaMetadata> searchMusicByAlbum(String query) {
        return searchMusic(MediaMetadata.METADATA_KEY_ALBUM, query);
    }

    public Iterable<MediaMetadata> searchMusicByArtist(String query) {
        return searchMusic(MediaMetadata.METADATA_KEY_ARTIST, query);
    }

    Iterable<MediaMetadata> searchMusic(String metadataField, String query) {
        if (mCurrentState != State.INITIALIZED) {
            return Collections.emptyList();
        }
        ArrayList<MediaMetadata> result = new ArrayList<>();
        query = query.toLowerCase(Locale.US);
        for (MutableMediaMetadata track : mMusicListById.values()) {
            if (track.metadata.getString(metadataField).toLowerCase(Locale.US)
                    .contains(query)) {
                result.add(track.metadata);
            }
        }
        return result;
    }

    public MediaMetadata getMusic(String musicId) {
        return mMusicListById.containsKey(musicId) ? mMusicListById.get(musicId).metadata : null;
    }

    public synchronized void updateMusicArt(String musicId, Bitmap albumArt, Bitmap icon) {
        MediaMetadata metadata = getMusic(musicId);
        metadata = new MediaMetadata.Builder(metadata)
                .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, albumArt)
                .putBitmap(MediaMetadata.METADATA_KEY_DISPLAY_ICON, icon)
                .build();

        MutableMediaMetadata mutableMetadata = mMusicListById.get(musicId);
        if (mutableMetadata == null) {
            throw new IllegalStateException("Unexpected error: Inconsistent data structures in " +
                    "MusicProvider");
        }

        mutableMetadata.metadata = metadata;
    }

    public boolean isInitialized() {
        return mCurrentState == State.INITIALIZED;
    }

    public void retrieveMediaAsync(final Callback callback) {
        if (mCurrentState == State.INITIALIZED) {
            if (callback != null) {
                callback.onMusicCatalogReady(true);
            }
            return;
        }
        mMusicListByGenre.clear();
        mMusicListById.clear();
        new AsyncTask<Void, Void, State>() {
            @Override
            protected State doInBackground(Void... params) {
                retrieveMedia();
                return mCurrentState;
            }

            @Override
            protected void onPostExecute(State current) {
                if (callback != null) {
                    callback.onMusicCatalogReady(current == State.INITIALIZED);
                }
            }
        }.execute();
    }

    private synchronized void buildListsByGenre() {
        ConcurrentMap<String, List<MediaMetadata>> newMusicListByGenre = new
                ConcurrentHashMap<>();

        for (MutableMediaMetadata m : mMusicListById.values()) {
            String genre = m.metadata.getString(MediaMetadata.METADATA_KEY_GENRE);
            List<MediaMetadata> list = newMusicListByGenre.get(genre);
            if (list == null) {
                list = new ArrayList<>();
                newMusicListByGenre.put(genre, list);
            }
            list.add(m.metadata);
        }
        mMusicListByGenre = newMusicListByGenre;
    }

    private synchronized void retrieveMedia() {
        try {
            if (mCurrentState == State.NON_INITIALIZED) {
                mCurrentState = State.INITIALIZING;

                Iterator<MediaMetadata> tracks = mSource.iterator();
                while (tracks.hasNext()) {
                    MediaMetadata item = tracks.next();
                    String musicId = item.getString(MediaMetadata.METADATA_KEY_MEDIA_ID);
                    mMusicListById.put(musicId, new MutableMediaMetadata(musicId, item));
                }
                buildListsByGenre();
                mCurrentState = State.INITIALIZED;
            }
        } finally {
            if (mCurrentState != State.INITIALIZED) {
                mCurrentState = State.NON_INITIALIZED;
            }
        }
    }


    public List<MediaBrowser.MediaItem> getChildren(String mediaId, Resources resources) {
        List<MediaBrowser.MediaItem> mediaItems = new ArrayList<>();

        if (!MediaIDHelper.isBrowseable(mediaId)) {
            return mediaItems;
        }
        if (MEDIA_ID_ROOT.equals(mediaId)) {
            for (String genre : getGenres()) {
                for (MediaMetadata metadata : mMusicListByGenre.get(genre)) {
                    LogUtils.d("getChildren::" + metadata.getString(MediaMetadata.METADATA_KEY_TITLE));
                    mediaItems.add(createMediaItem(metadata));
                }
            }

            Collections.reverse(mediaItems);

        } else if (MEDIA_ID_MUSICS_BY_GENRE.equals(mediaId)) {
            for (String genre : getGenres()) {
                mediaItems.add(createBrowsableMediaItemForGenre(genre, resources));
            }

        } else if (mediaId.startsWith(MEDIA_ID_MUSICS_BY_GENRE)) {
            String genre = MediaIDHelper.getHierarchy(mediaId)[1];
            for (MediaMetadata metadata : getMusicsByGenre(genre)) {
                mediaItems.add(createMediaItem(metadata));
            }

        } else {

        }
        return mediaItems;
    }

    private MediaBrowser.MediaItem createBrowsableMediaItemForRoot(Resources resources) {
        MediaDescription description = new MediaDescription.Builder()
                .setMediaId(MEDIA_ID_MUSICS_BY_GENRE)
                .setTitle("genres")
                .setSubtitle("genre_subtitle")
                .setIconUri(Uri.parse("android.resource://" +
                        "com.example.android.uamp/drawable/ic_by_genre"))
                .build();
        return new MediaBrowser.MediaItem(description,
                MediaBrowser.MediaItem.FLAG_BROWSABLE);
    }

    private MediaBrowser.MediaItem createBrowsableMediaItemForGenre(String genre,
                                                                          Resources resources) {
        MediaDescription description = new MediaDescription.Builder()
                .setMediaId(createMediaID(null, MEDIA_ID_MUSICS_BY_GENRE, genre))
                .setTitle(genre)
                .setSubtitle("genre_subtitle, genre")
                .build();
        return new MediaBrowser.MediaItem(description,
                MediaBrowser.MediaItem.FLAG_BROWSABLE);
    }

    private MediaBrowser.MediaItem createMediaItem(MediaMetadata metadata) {
        String genre = metadata.getString(MediaMetadata.METADATA_KEY_GENRE);
        String hierarchyAwareMediaID = createMediaID(
                metadata.getDescription().getMediaId(), MEDIA_ID_MUSICS_BY_GENRE, genre);
        MediaMetadata copy = new MediaMetadata.Builder(metadata)
                .putString(MediaMetadata.METADATA_KEY_MEDIA_ID, hierarchyAwareMediaID)
                .build();
        return new MediaBrowser.MediaItem(copy.getDescription(),
                MediaBrowser.MediaItem.FLAG_PLAYABLE);

    }

}
