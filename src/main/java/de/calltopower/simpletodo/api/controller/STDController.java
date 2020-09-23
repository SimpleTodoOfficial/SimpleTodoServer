package de.calltopower.simpletodo.api.controller;

/**
 * A REST controller
 */
public interface STDController {

    /**
     * Use this API path for the controller paths
     */
    public static String APIPATH = "/api";

    /**
     * Returns the "global" path of the controller
     * 
     * @return The "global" path of the controller
     */
    String getPath();

}
