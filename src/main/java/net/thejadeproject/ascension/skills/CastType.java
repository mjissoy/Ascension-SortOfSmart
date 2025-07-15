package net.thejadeproject.ascension.skills;
/*
    INSTANT -> no cast time
    LONG -> casts after a set time
    CONTINUOUS -> produces an effect as long as a condition is true. e.g enough qi or cast time not over yet
 */

public enum CastType {
    NONE,
    INSTANT,
    LONG,
    CONTINUOUS
}
