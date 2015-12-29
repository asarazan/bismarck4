package com.levelmoney.bismarck4

/**
 * Created by Aaron Sarazan on 12/29/15.
 * Copyright(c) 2015 Level, Inc.
 */
enum class BismarckState {
    Fresh,
    Stale,
    Fetching,
    Error
}