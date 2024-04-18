package com.example.androiddemo.model

import com.google.gson.annotations.SerializedName


data class TopicSubmissions (

  @SerializedName("street-photography"    ) var streetPhotography    : StreetPhotography?    = StreetPhotography(),
  @SerializedName("wallpapers"            ) var wallpapers            : Wallpapers?            = Wallpapers(),
  @SerializedName("travel"                ) var travel                : Travel?                = Travel(),
  @SerializedName("architecture-interior" ) var architectureInterior : ArchitectureInterior? = ArchitectureInterior()

)