package doma.hr.repository;

import doma.hr.model.User;

import java.util.List;

public interface IAdminRepository {

    List<User> getUnconfirmedUsers();
}
