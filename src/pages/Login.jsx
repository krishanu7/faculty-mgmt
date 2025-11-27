import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { loginEmployee } from '../api'

import { GoogleLogin } from '@react-oauth/google'

export default function Login() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)
  const navigate = useNavigate()

  async function onSubmit(e) {
    e.preventDefault()
    setError(null)
    try {
      const emp = await loginEmployee(email, password)
      localStorage.setItem('employeeId', emp.id)
      navigate(`/employee/${emp.id}/courses`)
    } catch (err) {
      setError(err.message)
    }
  }

  const handleGoogleSuccess = async (credentialResponse) => {
    try {
      const emp = await loginEmployee(null, null, credentialResponse.credential)
      localStorage.setItem('employeeId', emp.id)
      navigate(`/employee/${emp.id}/courses`)
    } catch (err) {
      setError("Google Login Failed: " + err.message)
    }
  }

  return (
    <div className="login-card">
      <h2 className="login-title">Employee Login</h2>
      <form onSubmit={onSubmit} className="login-form">
        <label className="login-label">
          <span className="login-label-text">Email</span>
          <input className="login-input" type="email" value={email} onChange={e => setEmail(e.target.value)} required />
        </label>
        <label className="login-label">
          <span className="login-label-text">Password</span>
          <input className="login-input" type="password" value={password} onChange={e => setPassword(e.target.value)} required />
        </label>
        <div>
          <button className="btn btn-primary" type="submit">Login</button>
        </div>
        <div style={{ marginTop: '1rem' }}>
          <GoogleLogin
            onSuccess={handleGoogleSuccess}
            onError={() => {
              setError('Google Login Failed');
            }}
          />
        </div>
        {error && <p className="text-error">{error}</p>}
      </form>
    </div>
  )
}
