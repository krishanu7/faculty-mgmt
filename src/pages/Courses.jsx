import React, { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { getCourses } from '../api'

export default function Courses() {
  const { id } = useParams()
  const employeeId = id || localStorage.getItem('employeeId')
  const [courses, setCourses] = useState([])
  const [error, setError] = useState(null)

  useEffect(() => {
    if (!employeeId) return
    getCourses(employeeId).then(setCourses).catch(e => setError(e.message))
  }, [employeeId])

  if (error) return (
    <div className="page" style={{ textAlign: 'center', marginTop: '4rem' }}>
      <div className="text-error" style={{ fontSize: '1.25rem' }}>{error}</div>
    </div>
  )

  return (
    <div className="page">
      <div style={{ marginBottom: '2rem' }}>
        <h2 style={{
          fontSize: '2rem',
          fontWeight: '700',
          background: 'linear-gradient(to right, var(--primary), var(--secondary))',
          WebkitBackgroundClip: 'text',
          WebkitTextFillColor: 'transparent',
          marginBottom: '0.5rem'
        }}>
          Your Courses
        </h2>
        <p style={{ color: 'var(--text-muted)' }}>Manage your assigned courses and students</p>
      </div>

      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))',
        gap: '2rem'
      }}>
        {courses.map(c => (
          <div key={c.id} className="card" style={{
            padding: '0',
            display: 'flex',
            flexDirection: 'column',
            height: '100%',
            overflow: 'hidden'
          }}>
            <div style={{ padding: '1.5rem', flex: 1, textAlign: 'left' }}>
              <div style={{
                fontSize: '0.875rem',
                fontWeight: '600',
                color: 'var(--primary)',
                textTransform: 'uppercase',
                letterSpacing: '0.05em',
                marginBottom: '0.75rem'
              }}>
                {c.code}
              </div>
              <h3 style={{
                fontSize: '1.25rem',
                fontWeight: '600',
                margin: 0,
                lineHeight: '1.4'
              }}>
                {c.name}
              </h3>
            </div>

            <div style={{
              borderTop: '1px solid var(--border)',
              padding: '1rem',
              display: 'flex',
              background: 'var(--background)'
            }}>
              <Link
                to={`/courses/${c.id}/students`}
                className="btn btn-primary"
                style={{
                  width: '100%',
                  textDecoration: 'none',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center'
                }}
              >
                View Students
              </Link>
            </div>
          </div>
        ))}
      </div>

      {courses.length === 0 && (
        <div className="card" style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-muted)' }}>
          <p style={{ fontSize: '1.1rem' }}>No courses found for this account.</p>
        </div>
      )}
    </div>
  )
}
