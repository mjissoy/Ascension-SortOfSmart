package net.thejadeproject.ascension.refactor_packages.skills;

import net.thejadeproject.ascension.refactor_packages.util.IDataInstance;

public interface IPersistentSkillData extends IDataInstance {

    IPersistentSkillData copy();

    IPersistentSkillData merge(IPersistentSkillData other);
}
