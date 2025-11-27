import React, { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { getStudents, postGrades } from '../api'

export default function CourseStudents() {
  const { id } = useParams()
  const courseId = id
  const employeeId = localStorage.getItem('employeeId')
  const [students, setStudents] = useState([])
  const [grades, setGrades] = useState({})
  const [selected, setSelected] = useState({})
  const [selectAll, setSelectAll] = useState(false)
  const [bulkMarks, setBulkMarks] = useState('')
  const [error, setError] = useState(null)
  const [message, setMessage] = useState(null)

  useEffect(() => {
    setMessage(null)
    if (!courseId) return
    getStudents(courseId).then(data => setStudents(data)).catch(e => setError(e.message))
  }, [courseId])

  function onChange(sid, value) {
    setGrades(prev => ({ ...prev, [sid]: value }))
  }

  function toggleSelect(sid) {
    setSelected(prev => ({ ...prev, [sid]: !prev[sid] }))
  }

  function toggleSelectAll() {
    const next = !selectAll
    setSelectAll(next)
    const newSelected = {}
    if (next) {
      students.forEach(s => { newSelected[s.id] = true })
    }
    setSelected(newSelected)
  }

  function applyToSelected() {
    if (bulkMarks === '') return
    const value = bulkMarks
    const updated = { ...grades }
    Object.keys(selected).forEach(sid => {
      if (selected[sid]) updated[sid] = value
    })
    setGrades(updated)
    setMessage('Applied marks to selected students')
  }

  async function submitGrades() {
    setError(null)
    setMessage(null)
    const payload = Object.keys(grades).map(sid => ({ studentId: Number(sid), marks: Number(grades[sid]) }))
    try {
      const updated = await postGrades(courseId, Number(employeeId), payload)
      setStudents(updated)
      setMessage('Grades saved')
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <div>
          <h2 style={{ fontSize: '1.875rem', fontWeight: '700', marginBottom: '0.5rem' }}>Course Students</h2>
          <p style={{ color: 'var(--text-muted)' }}>Manage grades for course ID: {courseId}</p>
        </div>
        <Link to={`/employee/${employeeId}/courses`} className="btn" style={{ background: 'white', border: '1px solid var(--border)' }}>
          ← Back to Courses
        </Link>
      </div>

      <div className="card" style={{ padding: '0' }}>
        <div
          style={{
            padding: '1.5rem',
            borderBottom: '1px solid var(--border)',
            background: 'var(--background)'
          }}
        >
          <div
            className="bulk-controls"
            style={{
              margin: 0,
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              width: '100%'
            }}
          >
            {/* LEFT — Select All */}
            <label style={{ cursor: 'pointer', fontWeight: '500', display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
              <input
                type="checkbox"
                checked={selectAll}
                onChange={toggleSelectAll}
                style={{ width: '1.1rem', height: '1.1rem', cursor: 'pointer' }}
              />
              Select All
            </label>

            {/* RIGHT — Marks + Apply Button */}
            <div style={{ display: 'flex', gap: '0.5rem', alignItems: 'center' }}>
              <input
                className="input-small"
                placeholder="Marks"
                value={bulkMarks}
                onChange={e => setBulkMarks(e.target.value)}
                style={{ width: '80px' }}
              />
              <button
                className="btn btn-primary"
                onClick={applyToSelected}
                style={{ padding: '0.4rem 0.8rem', fontSize: '0.875rem' }}
              >
                Apply to Selected
              </button>
            </div>
          </div>
        </div>

        {error && (
          <div style={{ padding: '1rem', background: '#fee2e2', color: '#b91c1c', borderBottom: '1px solid #fecaca' }}>
            {error}
          </div>
        )}
        {message && (
          <div style={{ padding: '1rem', background: '#d1fae5', color: '#047857', borderBottom: '1px solid #a7f3d0' }}>
            {message}
          </div>
        )}

        <div className="table-wrap">
          <table className="students-table">
            <thead>
              <tr>
                <th style={{ width: '50px', textAlign: 'center' }}>Select</th>
                <th>Roll Number</th>
                <th>Student Name</th>
                <th>Current Marks</th>
                <th>Update Marks</th>
              </tr>
            </thead>
            <tbody>
              {students.map(s => (
                <tr key={s.id} style={{ background: selected[s.id] ? 'rgba(79, 70, 229, 0.05)' : 'transparent' }}>
                  <td style={{ textAlign: 'center' }}>
                    <input
                      type="checkbox"
                      checked={!!selected[s.id]}
                      onChange={() => toggleSelect(s.id)}
                      style={{ width: '1.1rem', height: '1.1rem', cursor: 'pointer' }}
                    />
                  </td>
                  <td style={{ fontFamily: 'monospace', fontSize: '1rem' }}>{s.rollNumber}</td>
                  <td style={{ fontWeight: '500' }}>{s.firstName} {s.lastName}</td>
                  <td>
                    <span
                      style={{
                        display: 'inline-block',
                        padding: '0.25rem 0.75rem',
                        borderRadius: '999px',
                        background: s.marks !== null ? '#e0e7ff' : '#f3f4f6',
                        color: s.marks !== null ? '#3730a3' : '#6b7280',
                        fontSize: '0.875rem',
                        fontWeight: '500'
                      }}
                    >
                      {s.marks ?? 'Not Graded'}
                    </span>
                  </td>
                  <td>
                    <input
                      className="input-small"
                      type="number"
                      step="0.1"
                      value={grades[s.id] ?? (s.marks ?? '')}
                      onChange={e => onChange(s.id, e.target.value)}
                      placeholder="-"
                      style={{ width: '80px' }}
                    />
                  </td>
                </tr>
              ))}

              {students.length === 0 && (
                <tr>
                  <td colSpan="5" style={{ textAlign: 'center', padding: '3rem', color: 'var(--text-muted)' }}>
                    No students enrolled in this course.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>


      <div style={{ marginTop: '2rem', display: 'flex', justifyContent: 'flex-end' }}>
        <button
          className="btn btn-primary"
          onClick={submitGrades}
          style={{ padding: '0.75rem 2rem', fontSize: '1rem' }}
        >
          Save All Grades
        </button>
      </div>
    </div>
  )
}
