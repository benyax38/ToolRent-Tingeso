import httpClient from "../../http-common";

const registerUser = data => {
  return httpClient.post("/api/users/register", data);
}

const loginUser = async (data) => {
  const res = await httpClient.post("/api/users/login", data);
  return res.data;
}

export default { registerUser, loginUser };

