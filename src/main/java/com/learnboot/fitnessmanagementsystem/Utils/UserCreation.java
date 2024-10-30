package com.learnboot.fitnessmanagementsystem.Utils;

import com.learnboot.fitnessmanagementsystem.domains.User;
import com.learnboot.fitnessmanagementsystem.request.UserCreationRequest;

public interface UserCreation {

    public User createUser(UserCreationRequest user);
}
