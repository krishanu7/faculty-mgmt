import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { loginEmployee } from '../api'

export default function Login(){
  const [id, setId] = useState('')
  const [error, setError] = useState(null)
  const navigate = useNavigate()

  async function onSubmit(e){
    e.preventDefault()
    setError(null)
    try{
      const emp = await loginEmployee(Number(id))
      localStorage.setItem('employeeId', emp.id || id)
      navigate(`/employee/${emp.id || id}/courses`)
    } catch(err){
      setError(err.message)
    }
  }

  return (
    <div className="login-card">
      <h2 className="login-title">Employee Login</h2>
      <form onSubmit={onSubmit} className="login-form">
        <label className="login-label">
          <span className="login-label-text">Employee ID</span>
          <input className="login-input" value={id} onChange={e => setId(e.target.value)} />
        </label>
        <div>
          <button className="btn btn-primary" type="submit">Login</button>
        </div>
        {error && <p className="text-error">{error}</p>}
      </form>
      <p className="login-hint">Use numeric employee id (e.g. 1)</p>
    </div>
  )
}
