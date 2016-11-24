package com.nerminturkovic.flickrtestapp.data.transformer;

import com.nerminturkovic.flickrtestapp.data.model.Location;
import com.nerminturkovic.flickrtestapp.data.model.Owner;
import com.nerminturkovic.flickrtestapp.data.model.Photo;
import com.nerminturkovic.flickrtestapp.data.model.PhotoSize;
import com.nerminturkovic.flickrtestapp.data.remote.network.model.Size;

/**
 * Created by nerko on 23/11/16.
 */

public class NetworkToModelTransformer {

    public Photo photoNetworkToPhoto(com.nerminturkovic.flickrtestapp.data.remote.network.model.Photo photoNetwork) {
        Photo photo = new Photo();

        photo.setFlickrId(photoNetwork.getId());
        photo.setTitle(photoNetwork.getTitle().getContent());
        photo.setOwner(this.ownerNetworkToOwner(photoNetwork.getOwner()));
        if (photoNetwork.getLocation() != null) {
            photo.setLocation(this.locationNetworkToLocation(photoNetwork.getLocation()));
        }

        return photo;
    }

    public Owner ownerNetworkToOwner(com.nerminturkovic.flickrtestapp.data.remote.network.model.Owner ownerNetwork) {
        Owner owner = new Owner();

        owner.setFlickrId(ownerNetwork.getNsid());
        owner.setUsername(ownerNetwork.getUsername());
        owner.setRealName(ownerNetwork.getRealname());

        return owner;
    }

    public Location locationNetworkToLocation(com.nerminturkovic.flickrtestapp.data.remote.network.model.Location locationNetwork) {
        Location location = new Location();

        location.setLatitude(Float.valueOf(locationNetwork.getLatitude()));
        location.setLongitude(Float.valueOf(locationNetwork.getLongitude()));
        location.setLocality(locationNetwork.getLocality().getContent());
        location.setCounty(locationNetwork.getCounty().getContent());
        location.setRegion(locationNetwork.getRegion().getContent());
        location.setCountry(locationNetwork.getCountry().getContent());

        return location;
    }

    public PhotoSize sizeNetworkToPhotoSize(Size sizeNetwork) {
        PhotoSize size = new PhotoSize();

        size.setLabel(sizeNetwork.getLabel());
        size.setWidth(Integer.valueOf(sizeNetwork.getWidth()));
        size.setHeight(Integer.valueOf(sizeNetwork.getHeight()));
        size.setSource(sizeNetwork.getSource());
        size.setUrl(sizeNetwork.getUrl());

        return size;
    }
}
