import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import '../App.css';

export default function Navbar() {
    const navigate = useNavigate();
    const location = useLocation();
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    // Check login status whenever location changes (navigation) or on mount
    useEffect(() => {
        const empId = localStorage.getItem('employeeId');
        setIsLoggedIn(!!empId);
    }, [location]);

    const handleLogout = () => {
        localStorage.removeItem('employeeId');
        setIsLoggedIn(false);
        navigate('/login');
    };

    return (
        <nav className="navbar">
            <div className="navbar-container">
                <Link to="/" className="navbar-logo">
                    Faculty<span className="text-primary">Manager</span>
                </Link>

                <div className="navbar-links">
                    <Link to="/" className={`nav-link ${location.pathname === '/' ? 'active' : ''}`}>Home</Link>
                    <Link to="/about" className={`nav-link ${location.pathname === '/about' ? 'active' : ''}`}>About</Link>

                    {isLoggedIn ? (
                        <>
                            <button onClick={handleLogout} className="btn btn-outline-danger">
                                Logout
                            </button>
                        </>
                    ) : (
                        <Link to="/login" className="btn btn-primary">
                            Login
                        </Link>
                    )}
                </div>
            </div>
        </nav>
    );
}
