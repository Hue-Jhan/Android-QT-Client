package com.example.client2.ui;

import java.io.Serializable;

public class ServerException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public ServerException(String message) {
        super(message);
    }
}

