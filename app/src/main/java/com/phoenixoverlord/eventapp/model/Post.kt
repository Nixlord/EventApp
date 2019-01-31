package com.phoenixoverlord.eventapp.model

import java.util.*

/*
Post/
user_id
title?
caption(or content)?
image_id?
tags
date_time
#likes
#shares
comment_id
*/

class Post(
    var userID : String = "",
    var postID : String = "",
    var title : String? = null,
    var content : String = "",
    var imageID : String? = null,
    var tags : ArrayList<String> = arrayListOf(),
    var date: Date = Date(),
    var likedByUIDs: ArrayList<String> = arrayListOf(),
    var commentID : String = ""
)