package com.example.androiddemo.model

import com.example.ResponseMain.AlternativeSlugs
import com.example.ResponseMain.Links
import com.example.ResponseMain.TopicSubmissions
import com.example.ResponseMain.Urls
import com.example.ResponseMain.User
import com.google.gson.annotations.SerializedName

data class ResponseMain(@SerializedName("id"                       ) var id                     : String?           = null,
                        @SerializedName("slug"                     ) var slug                   : String?           = null,
                        @SerializedName("alternative_slugs"        ) var alternativeSlugs       : AlternativeSlugs? = AlternativeSlugs(),
                        @SerializedName("created_at"               ) var createdAt              : String?           = null,
                        @SerializedName("updated_at"               ) var updatedAt              : String?           = null,
                        @SerializedName("promoted_at"              ) var promotedAt             : String?           = null,
                        @SerializedName("width"                    ) var width                  : Int?              = null,
                        @SerializedName("height"                   ) var height                 : Int?              = null,
                        @SerializedName("color"                    ) var color                  : String?           = null,
                        @SerializedName("blur_hash"                ) var blurHash               : String?           = null,
                        @SerializedName("description"              ) var description            : String?           = null,
                        @SerializedName("alt_description"          ) var altDescription         : String?           = null,
                        @SerializedName("breadcrumbs"              ) var breadcrumbs            : List<Breadcrums> = arrayListOf(),
                        @SerializedName("urls"                     ) var urls                   : Urls?             = Urls(),
                        @SerializedName("links"                    ) var links                  : Links?            = Links(),
                        @SerializedName("likes"                    ) var likes                  : Int?              = null,
                        @SerializedName("liked_by_user"            ) var likedByUser            : Boolean?          = null,
                        @SerializedName("current_user_collections" ) var currentUserCollections : List<String> = arrayListOf(),
                        @SerializedName("topic_submissions"        ) var topicSubmissions       : TopicSubmissions? = TopicSubmissions(),
                        @SerializedName("asset_type"               ) var assetType              : String?           = null,
                        @SerializedName("user"                     ) var user                   : User?             = User())
