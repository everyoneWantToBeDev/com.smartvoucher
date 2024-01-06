import { Routes, Route } from "react-router-dom";
import HomePage from "../components/Home/HomePage";
import NotFound from "../components/Error/NotFound";
import PrivateRoutes from "../Routes/PrivateRoutes";
import User from "../components/User/User";

const AppRoutes = () => {
  return (
    <>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route
          path="/User/*"
          element={<PrivateRoutes Component={User}  />}
        />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </>
  );
};

export default AppRoutes;
