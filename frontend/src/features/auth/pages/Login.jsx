import { useState, useEffect } from "react";
import { useAuth } from "../../../app/providers/AuthContext"
import { useNavigate } from "react-router-dom"
import { AuthForm } from "../components/layout/AuthForm/AuthForm";
import { AuthLayout } from "../components/layout/AuthLayout/AuthLayout";
import { Link } from "react-router-dom";

export const Login = () => {
    const { login } = useAuth();
    const navigate = useNavigate();
    const [credentials, setCredentials] = useState({
        email: "",
        password: ""
    });
    const [error, setError] = useState();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch("http://localhost:8080/auth/login",
                {
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify(credentials),
                }
            );

            if(response.ok){
                const data = await response.json();
                login(data.token);
                navigate("/dashboard");
            } else{
                const errorData = await response.json();
                if (response.status === 403 && errorData.message === "Email is not verified. Please check your inbox.") {
                    sessionStorage.setItem("email", credentials.email);
                    navigate("/verify-email");
                } else if (response.status === 401){
                    alert(errorData.message);
                } else {
                    setError(errorData.message);
                    const messages = Object.values(errorData.errors).join("\n");
                    alert(messages);
                }
            }
        } catch (error){
            console.log(error);
        }
    }

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setCredentials(
            {
            ...credentials,
            [name]: value,
            }
        );
    };

    const fields = [
    {
        name: "email",
        type: "email",
        value: credentials.email,
        onChange: handleInputChange,
        placeholder: "Email",
    },
    {
        name: "password",
        type: "password",
        value: credentials.password,
        onChange: handleInputChange,
        placeholder: "Password",
    },
    ];

    return (
        <AuthLayout>
            <AuthForm 
                title="Sign in"
                fields={fields}
                submitText="Sign in"
                onSubmit={handleSubmit}
                footer={
                    <>
                        <p>Don't have an account? <Link to="/register">Join now</Link></p>
                        <br />
                        <p>-- Or -- </p>
                        <br />
                        <p>Forgot password? <Link to="/reset-password">Reset password</Link></p>
                    </>
                }
                />
        </AuthLayout>
    );
}