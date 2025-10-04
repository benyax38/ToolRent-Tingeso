import httpClient from "../../http-common";

const registerUser = data => {
  return httpClient.post("/api/users/register", data);
}

const loginUser = data => {
  return httpClient.post("/api/users/login", data);
}

export default { registerUser, loginUser };

