package com.example.androiddemo.model

import com.google.gson.annotations.SerializedName


data class Travel (

  @SerializedName("status"      ) var status     : String? = null,
  @SerializedName("approved_on" ) var approvedOn : String? = null

)