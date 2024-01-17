import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  avatarUrl: "",
  userName: "",
  id: 0,
};

export const UserSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    avatar: (state, action) => {
      state.avatarUrl = action.payload;
    },
    username: (state, action) => {
      state.userName = action.payload;
    },
    userId: (state, action) => {
      state.id = action.payload;
    },
  },
  extraReducers: (builder) => {},
});

export const { avatar, username, userId } = UserSlice.actions;

export const selectAvatar = (state) => state.user.avatarUrl;
export const selectUsername = (state) => state.user.userName;
export const selectUserId = (state) => state.user.id;

export default UserSlice.reducer;
