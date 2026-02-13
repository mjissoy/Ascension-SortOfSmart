package net.thejadeproject.ascension.refactor_packages.forms;

import net.thejadeproject.ascension.refactor_packages.util.IDataInstanceProvider;

public interface IEntityForm extends IDataInstanceProvider {

    void enterForm();
    void leaveForm();
}
