package com.github.matthewmartin117.healthcare_api.models;

// simple data holder for login requests
/* when posting JSON like:
{"username:"admin", "password":"admin123"}
it gets deserialized into an instance of this class
*/
public class AuthRequest {

  private String username;
  private String password;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}