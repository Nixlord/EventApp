package com.example.overlord.eventapp.firestore

/*

'?' -> (should we do that or skip that) OR (optional input for that attribute)

Classes that will be needed -
1. User - when a person logs in the app for first time

2. Comment - stores comment on basis of comment_id. Each comment_id
            refers to a particular image or blog and all the comments are
            added inside that id

3. Post - contains information about photos and blogs

4. Events - has information of all the events in the wedding

User/
    user_id = (name+phone_no)?
    name
    phone no
    email id?
    profile photo
    wedding side
    relation with couple
    key_contact (boolean)?

Comment/
    comment_id/
        list< user_id
              comment
              date_time>

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

Events/
    event_id
    name
    date
    location
    time
    message?
    image

 */