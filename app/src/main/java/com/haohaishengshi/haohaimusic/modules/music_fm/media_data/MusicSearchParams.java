package com.haohaishengshi.haohaimusic.modules.music_fm.media_data;

import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * @Author Jliuer
 * @Date 2017/2/21/14:04
 * @Email Jliuer@aliyun.com
 * @Description 搜索本地音乐，赞不启用
 */
public final class MusicSearchParams {

    public final String query;
    public boolean isAny;
    public boolean isUnstructured;
    public boolean isGenreFocus;
    public boolean isArtistFocus;
    public boolean isAlbumFocus;
    public boolean isSongFocus;
    public String genre;
    public String artist;
    public String album;
    public String song;

    public MusicSearchParams(String query, Bundle extras) {
        this.query = query;

        if (TextUtils.isEmpty(query)) {
            isAny = true;
        } else {
            if (extras == null) {
                isUnstructured = true;
            } else {
                String genreKey;
                if (Build.VERSION.SDK_INT >= 21) {
                    genreKey = MediaStore.EXTRA_MEDIA_GENRE;
                } else {
                    genreKey = "android.intent.extra.genre";
                }

                String mediaFocus = extras.getString(MediaStore.EXTRA_MEDIA_FOCUS);
                if (TextUtils.equals(mediaFocus, MediaStore.Audio.Genres.ENTRY_CONTENT_TYPE)) {

                    isGenreFocus = true;
                    genre = extras.getString(genreKey);
                    if (TextUtils.isEmpty(genre)) {
                        genre = query;
                    }
                } else if (TextUtils.equals(mediaFocus, MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE)) {
                    isArtistFocus = true;
                    genre = extras.getString(genreKey);
                    artist = extras.getString(MediaStore.EXTRA_MEDIA_ARTIST);
                } else if (TextUtils.equals(mediaFocus, MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE)) {
                    isAlbumFocus = true;
                    album = extras.getString(MediaStore.EXTRA_MEDIA_ALBUM);
                    genre = extras.getString(genreKey);
                    artist = extras.getString(MediaStore.EXTRA_MEDIA_ARTIST);
                } else if (TextUtils.equals(mediaFocus, MediaStore.Audio.Media.ENTRY_CONTENT_TYPE)) {
                    isSongFocus = true;
                    song = extras.getString(MediaStore.EXTRA_MEDIA_TITLE);
                    album = extras.getString(MediaStore.EXTRA_MEDIA_ALBUM);
                    genre = extras.getString(genreKey);
                    artist = extras.getString(MediaStore.EXTRA_MEDIA_ARTIST);
                } else {
                    isUnstructured = true;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "query=" + query
            + " isAny=" + isAny
            + " isUnstructured=" + isUnstructured
            + " isGenreFocus=" + isGenreFocus
            + " isArtistFocus=" + isArtistFocus
            + " isAlbumFocus=" + isAlbumFocus
            + " isSongFocus=" + isSongFocus
            + " genre=" + genre
            + " artist=" + artist
            + " album=" + album
            + " song=" + song;
    }

}
