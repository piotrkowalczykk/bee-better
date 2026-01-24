import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { Dashboard } from "../../features/customer/pages/Dashboard/Dashboard";
import { Unauthorized } from "../../features/auth/pages/Unauthorized";
import { RoleBasedRoute } from "../routes/RoleBasedRoute";
import { Login } from "../../features/auth/pages/Login";
import { Home } from "../../features/landing/pages/Home/Home"
import { Register } from "../../features/auth/pages/Register";
import { VerifyEmail } from "../../features/auth/pages/VerifyEmail";
import { ResetPassword } from "../../features/auth/pages/ResetPassword";
import { PublicRoute } from "./PublicRoute";
import { Routines } from "../../features/customer/pages/Routines/Routines";
import { Exercises } from "../../features/customer/pages/Exercises/Exercises";
import { Feed } from "../../features/customer/pages/Feed/Feed";
import { DayCreator } from "../../features/customer/pages/DayCreator/DayCreator";
import { ArticleDetails } from "../../features/customer/pages/ArticleDetails/ArticleDetails";
import { Admin } from "../../features/customer/pages/Admin/Admin";

export default function AppRoutes() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<PublicRoute><Home/></PublicRoute>} />
        <Route path="/login" element={<PublicRoute><Login/></PublicRoute>} />
        <Route path="/register" element={<PublicRoute><Register/></PublicRoute>} />
        <Route path="/verify-email" element={<PublicRoute><VerifyEmail/></PublicRoute>} />
        <Route path="/reset-password" element={<PublicRoute><ResetPassword/></PublicRoute>} />
        <Route path="/unauthorized" element={<Unauthorized/>} />
      
        <Route path="/dashboard" element={<RoleBasedRoute role="USER"><Dashboard /></RoleBasedRoute>} />
        <Route path="/routines" element={<RoleBasedRoute role="USER"><Routines /></RoleBasedRoute>} />
        <Route path="/exercises" element={<RoleBasedRoute role="USER"><Exercises /></RoleBasedRoute>} />
        <Route path="/feed" element={<RoleBasedRoute role="USER"><Feed /></RoleBasedRoute>} />
        <Route path="/day-creator" element={<RoleBasedRoute role="USER"><DayCreator /></RoleBasedRoute>} />
        <Route path="/articles/:articleId" element={<RoleBasedRoute role="USER"><ArticleDetails /></RoleBasedRoute>} />
        <Route path="/manage-articles" element={<RoleBasedRoute role="ADMIN"><Admin /></RoleBasedRoute>} />
      </Routes>
    </Router>
  )
}

