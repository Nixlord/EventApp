package com.example.overlord.eventapp.model

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
    var likes : Int = 0,
    var shares : Int = 0,
    var commentID : String = ""
)