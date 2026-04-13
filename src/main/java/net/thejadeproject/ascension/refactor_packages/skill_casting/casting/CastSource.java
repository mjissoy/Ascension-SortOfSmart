package net.thejadeproject.ascension.refactor_packages.skill_casting.casting;

//spells can only be used by players
//but we still want to know HOW the player cast it
public enum CastSource {
    PAYER,
    ITEM,
    COMMAND,
    NONE
}

