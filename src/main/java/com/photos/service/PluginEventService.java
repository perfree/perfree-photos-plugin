package com.photos.service;

import com.perfree.plugin.PluginEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PluginEventService implements PluginEvent {
    @Autowired
    private PhotosService photosService;

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onInstall() {
        photosService.dropTable();
        photosService.createTable();
    }

    @Override
    public void onUnInstall() {
        photosService.dropTable();
    }
}
