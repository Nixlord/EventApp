package com.example.overlord.eventapp.firestore

/*

'?' -> (should we do that or skip that) OR (optional input for that attribute)

Classes that will be needed -
1. User - when a person logs in the app for first time

On clicking a photo using camera does not upload photo to firestore
2. Photo - when a person uploads a photo from their gallery to the app

3. Comment - stores comment on basis of comment_id. Each comment_id
            refers to a particular image or blog and all the comments are
            added inside that id

Not all photos uploaded to the app are added to the wall
4. WallPhoto - if the person wants to add the photo to the wall

5. WallBlog - if person wants to create a new blog for the wall

6. Album - Stores all the photos inside the
            album according to the tags of the photos

7. Events - has information of all the events in the wedding

User/
    user_id = (name+phone_no)?
    name
    phone no
    email id?
    profile photo
    wedding side
    relation with couple
    key_contact (boolean)?

Photo/
    user_id
    image_id = timestamp?
    tag
    caption
    date_time
    present_in_wall (boolean)
    people_present_in_photo/
                        list<user_id>

Comment/
    comment_id/
        list< user_id
              comment
              date_time>

WallPhoto/
    image_id
    #likes
    #shares
    comment_id

WallBlog/
    user_id
    title
    content
    hashtags
    date_time
    #likes
    #shares
    comment_id

Album/
    date_time (this should be the time when
                first photo of this album is uploaded to the app)
    tag(album name)/
            image_id

Events/
    event_id
    name
    date
    location
    time
    message?
    image



 */