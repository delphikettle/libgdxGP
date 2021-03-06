package ru.dk.gdxGP.GameWorld;

public class ParticleDef {
    public float minX, maxX, minY, maxY;
    public float minCharge = -1f, maxCharge = 1f;
    public float minVX = 0, maxVX = 0, minVY = 0, maxVY = 0;
    public float minMass = 0.000001f, maxMass = 100f;
    public float rMin = 0.5f, gMin = 0.5f, bMin = 0.5f, aMin = 0.75f;
    public float rMax = 1, gMax = 1, bMax = 1, aMax = 1;

    public ParticleDef(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }
}
