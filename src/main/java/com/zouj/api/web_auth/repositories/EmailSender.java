package com.zouj.api.web_auth.repositories;

import com.zouj.api.web_auth.entities.User;

public interface EmailSender {

    void send(String to, String email);
}
