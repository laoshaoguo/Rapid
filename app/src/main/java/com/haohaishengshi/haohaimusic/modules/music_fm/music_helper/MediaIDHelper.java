package com.haohaishengshi.haohaimusic.modules.music_fm.music_helper;

import android.content.Context;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import java.util.Arrays;

/**
 * @Author Jliuer
 * @Date 2017/2/19/14:10
 * @Email Jliuer@aliyun.com
 * @Description 音乐分类层级管理
 */
public class MediaIDHelper {

    public static final String MEDIA_ID_EMPTY_ROOT = "__EMPTY_ROOT__";
    public static final String MEDIA_ID_ROOT = "__ROOT__";
    public static final String MEDIA_ID_MUSICS_BY_GENRE = "__BY_GENRE__";
    public static final String MEDIA_ID_MUSICS_BY_SEARCH = "__BY_SEARCH__";

    private static final char CATEGORY_SEPARATOR = '/';
    private static final char LEAF_SEPARATOR = '|';

    public static String createMediaID(String musicID, String... categories) {
        StringBuilder sb = new StringBuilder();
        if (categories != null) {
            for (int i = 0; i < categories.length; i++) {
                if (!isValidCategory(categories[i])) {
                    throw new IllegalArgumentException("Invalid category: " + categories[0]);
                }
                sb.append(categories[i]);
                if (i < categories.length - 1) {
                    sb.append(CATEGORY_SEPARATOR);
                }
            }
        }
        if (musicID != null) {
            sb.append(LEAF_SEPARATOR).append(musicID);
        }
        return sb.toString();
    }

    private static boolean isValidCategory(String category) {
        return category == null || (
                category.indexOf(CATEGORY_SEPARATOR) < 0 &&
                        category.indexOf(LEAF_SEPARATOR) < 0);
    }

    public static String extractMusicIDFromMediaID(@NonNull String mediaID) {
        int pos = mediaID.indexOf(LEAF_SEPARATOR);
        if (pos >= 0) {
            return mediaID.substring(pos + 1);
        }
        return null;
    }

    public static String[] getHierarchy(@NonNull String mediaID) {
        int pos = mediaID.indexOf(LEAF_SEPARATOR);
        if (pos >= 0) {
            mediaID = mediaID.substring(0, pos);
        }
        return mediaID.split(String.valueOf(CATEGORY_SEPARATOR));
    }

    public static String extractBrowseCategoryValueFromMediaID(@NonNull String mediaID) {
        String[] hierarchy = getHierarchy(mediaID);
        if (hierarchy.length == 2) {
            return hierarchy[1];
        }
        return null;
    }

    public static boolean isBrowseable(@NonNull String mediaID) {
        return mediaID.indexOf(LEAF_SEPARATOR) < 0;
    }

    public static String getParentMediaID(@NonNull String mediaID) {
        String[] hierarchy = getHierarchy(mediaID);
        if (!isBrowseable(mediaID)) {
            return createMediaID(null, hierarchy);
        }
        if (hierarchy.length <= 1) {
            return MEDIA_ID_ROOT;
        }
        String[] parentHierarchy = Arrays.copyOf(hierarchy, hierarchy.length - 1);
        return createMediaID(null, parentHierarchy);
    }

    public static boolean isMediaItemPlaying(Context context,
                                             MediaBrowser.MediaItem mediaItem) {

        MediaController controller = ((FragmentActivity) context)
                .getMediaController();
        if (controller != null && controller.getMetadata() != null) {
            String currentPlayingMediaId = controller.getMetadata().getDescription()
                    .getMediaId();
            String itemMusicId = MediaIDHelper.extractMusicIDFromMediaID(
                    mediaItem.getDescription().getMediaId());
            if (currentPlayingMediaId != null
                    && TextUtils.equals(currentPlayingMediaId, itemMusicId)) {
                return true;
            }
        }
        return false;
    }
}
