package com.company.tomek.algorithms;

// basically this is almost the same as Point class from Java SE packages
public class Solution {
    private double x;
    private double y;

    public Solution(double x, double y) {
        this.x = x;
        this.y =y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "x: " + x + " y: " + y;
    }
}
