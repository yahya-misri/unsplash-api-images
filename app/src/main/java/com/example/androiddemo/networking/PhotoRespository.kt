package com.example.androiddemo.networking

class PhotoRespository(var clientId:String) {

    fun photoPagingSource() = PhotoDataSource(clientId)
}