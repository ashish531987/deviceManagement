package com.emergent.devicemgmt.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column( unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String fullName;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name="users_roles_assoc",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="role_id")})
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<DeviceUserAssoc> deviceUserAssocSet;

    public User(){}
    public Set<DeviceUserAssoc> getDeviceUserAssocSet(){
        return deviceUserAssocSet;
    }

    public void setDeviceUserAssocSet(Set<DeviceUserAssoc> deviceUserAssocSet){
        this.deviceUserAssocSet = deviceUserAssocSet;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Role> getRoles() {
        return roles;
    }
    public void addRole(Role role){
        if(roles.contains(role)) return;
        roles.add(role);
        role.addUser(this);
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
