package ru.dk.gdxGP.GameWorld;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.ArrayList;
import java.util.List;

public class ContactMultiListener implements ContactListener {
    private final List<ContactListener> contactListeners=new ArrayList<ContactListener>();
    public ContactMultiListener(){}
    public void addContactListener(ContactListener contactListener){
        contactListeners.add(contactListener);
    }
    public void removeContactListener(ContactListener contactListener){
        contactListeners.remove(contactListener);
    }
    @Override
    public void beginContact(Contact contact) {
        for (ContactListener contactListener : contactListeners) {
            contactListener.beginContact(contact);
        }
    }

    @Override
    public void endContact(Contact contact) {
        for (ContactListener contactListener : contactListeners) {
            contactListener.endContact(contact);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        for (ContactListener contactListener : contactListeners) {
            contactListener.preSolve(contact, oldManifold);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        for (ContactListener contactListener : contactListeners) {
            contactListener.postSolve(contact, impulse);
        }
    }
}
