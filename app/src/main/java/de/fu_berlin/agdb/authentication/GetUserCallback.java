package de.fu_berlin.agdb.authentication;

import de.fu_berlin.agdb.data.User;

interface GetUserCallback {

    /**
     * Invoked when background task is completed
     */

    public abstract void done(User returnedUser);
}
