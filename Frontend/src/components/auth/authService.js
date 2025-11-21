import httpClient from "../../http-common";

const registerUser = async (data) => {
  const res = await httpClient.post("/api/users/register", data);
  return res.data;
}

const loginUser = async (data) => {
  const res = await httpClient.post("/api/users/login", data);
  return res.data;
}

export default { registerUser, loginUser };

