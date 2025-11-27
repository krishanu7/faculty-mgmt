import React from 'react'
import { Link } from 'react-router-dom'

export default function Home() {
  const isLoggedIn = !!localStorage.getItem('employeeId');

  return (
    <div className="page home-page">
      <div className="hero-section" style={{ textAlign: 'center', padding: '4rem 0' }}>
        <h1 style={{ fontSize: '3.5rem', fontWeight: '800', marginBottom: '1.5rem', background: 'linear-gradient(to right, var(--primary), var(--accent))', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
          Faculty Management System
        </h1>
        <p style={{ fontSize: '1.25rem', color: 'var(--text-muted)', maxWidth: '600px', margin: '0 auto 2.5rem' }}>
          Streamline your academic workflow. Manage courses, track student performance, and handle grading with ease.
        </p>

        <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center' }}>
          {isLoggedIn ? (
            <Link to={`/employee/${localStorage.getItem('employeeId')}/courses`} className="btn btn-primary" style={{ padding: '0.75rem 1.5rem', fontSize: '1.1rem' }}>
              Go to Dashboard
            </Link>
          ) : (
            <>
              <Link to="/login" className="btn btn-primary" style={{ padding: '0.75rem 1.5rem', fontSize: '1.1rem' }}>
                Get Started
              </Link>
              <Link to="/about" className="btn" style={{ padding: '0.75rem 1.5rem', fontSize: '1.1rem', background: 'white', border: '1px solid var(--border)' }}>
                Learn More
              </Link>
            </>
          )}
        </div>
      </div>

      <div className="features-grid" style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '2rem', marginTop: '4rem' }}>
        <div className="card" style={{ padding: '2rem' }}>
          <h3 style={{ fontSize: '1.5rem', marginBottom: '1rem', color: 'var(--primary)' }}>Course Management</h3>
          <p style={{ color: 'var(--text-muted)' }}>Effortlessly organize and access all your assigned courses in one centralized dashboard.</p>
        </div>
        <div className="card" style={{ padding: '2rem' }}>
          <h3 style={{ fontSize: '1.5rem', marginBottom: '1rem', color: 'var(--secondary)' }}>Student Tracking</h3>
          <p style={{ color: 'var(--text-muted)' }}>Keep track of student enrollments and monitor their progress throughout the semester.</p>
        </div>
        <div className="card" style={{ fontSize: '1.5rem', padding: '2rem' }}>
          <h3 style={{ fontSize: '1.5rem', marginBottom: '1rem', color: 'var(--accent)' }}>Easy Grading</h3>
          <p style={{ color: 'var(--text-muted)' }}>Input and update grades quickly. Generate reports and analyze class performance.</p>
        </div>
      </div>
    </div>
  )
}
