package com.zionhuang.music.constants

import androidx.annotation.IntDef

object MediaConstants {
    const val EXTRA_QUEUE_DATA = "queue_data"
    const val EXTRA_SONG_ID = "song_id"
    const val EXTRA_SONG = "song"
    const val EXTRA_SONGS = "songs"
    const val EXTRA_ARTIST_ID = "artist_id"
    const val EXTRA_ARTIST = "artist"
    const val EXTRA_PLAYLIST_ID = "playlist_id"
    const val EXTRA_PLAYLIST = "playlist"
    const val EXTRA_SEARCH_FILTER = "search_filter"
    const val EXTRA_ARTWORK_TYPE = "artwork_type"
    const val EXTRA_DURATION = "duration"

    @IntDef(QUEUE_NONE, QUEUE_ALL_SONG, QUEUE_PLAYLIST, QUEUE_ARTIST, QUEUE_CHANNEL, QUEUE_YT_SINGLE, QUEUE_YT_SEARCH, QUEUE_YT_PLAYLIST)
    @Retention(AnnotationRetention.SOURCE)
    annotation class QueueType

    const val QUEUE_NONE = 0
    const val QUEUE_ALL_SONG = 1
    const val QUEUE_PLAYLIST = 2
    const val QUEUE_ARTIST = 3
    const val QUEUE_CHANNEL = 4
    const val QUEUE_YT_SINGLE = 5
    const val QUEUE_YT_SEARCH = 6
    const val QUEUE_YT_PLAYLIST = 7
    const val QUEUE_YT_CHANNEL = 8

    @IntDef(TYPE_SQUARE, TYPE_RECTANGLE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ArtworkType

    const val TYPE_SQUARE = 0
    const val TYPE_RECTANGLE = 1

    const val STATE_NOT_DOWNLOADED = 0
    const val STATE_PREPARING = 1
    const val STATE_DOWNLOADING = 2
    const val STATE_DOWNLOADED = 3
}