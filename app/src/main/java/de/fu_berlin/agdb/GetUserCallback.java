package de.fu_berlin.agdb;

interface GetUserCallback {

    /**
     * Invoked when background task is completed
     */

    public abstract void done(User returnedUser);
}
