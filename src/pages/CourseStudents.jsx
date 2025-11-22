import React, { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { getStudents, postGrades } from '../api'

export default function CourseStudents(){
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

  useEffect(()=>{
    setMessage(null)
    if(!courseId) return
    getStudents(courseId).then(data => setStudents(data)).catch(e => setError(e.message))
  },[courseId])

  function onChange(sid, value){
    setGrades(prev => ({...prev, [sid]: value}))
  }

  function toggleSelect(sid){
    setSelected(prev => ({...prev, [sid]: !prev[sid]}))
  }

  function toggleSelectAll(){
    const next = !selectAll
    setSelectAll(next)
    const newSelected = {}
    if(next){
      students.forEach(s => { newSelected[s.id] = true })
    }
    setSelected(newSelected)
  }

  function applyToSelected(){
    if(bulkMarks === '') return
    const value = bulkMarks
    const updated = {...grades}
    Object.keys(selected).forEach(sid => {
      if(selected[sid]) updated[sid] = value
    })
    setGrades(updated)
    setMessage('Applied marks to selected students')
  }

  async function submitGrades(){
    setError(null)
    setMessage(null)
    const payload = Object.keys(grades).map(sid => ({ studentId: Number(sid), marks: Number(grades[sid]) }))
    try{
      const updated = await postGrades(courseId, Number(employeeId), payload)
      setStudents(updated)
      setMessage('Grades saved')
    }catch(err){
      setError(err.message)
    }
  }

  return (
    <div className="students-card">
      <div className="students-header">
        <h2 className="students-title">Students in course {courseId}</h2>
        <Link className="link" to={`/employee/${employeeId}/courses`}>Back to courses</Link>
      </div>
      <div className="bulk-controls">
        <label><input type="checkbox" checked={selectAll} onChange={toggleSelectAll} /> Select all</label>
        <input className="input-small" placeholder="Marks" value={bulkMarks} onChange={e => setBulkMarks(e.target.value)} />
        <button className="btn" onClick={applyToSelected}>Apply to selected</button>
      </div>
      {error && <p className="text-error">{error}</p>}
      {message && <p className="text-success">{message}</p>}
      <div className="table-wrap">
        <table className="students-table">
          <thead>
            <tr className="text-left">
              <th className="cell">Roll</th>
              <th className="cell">Name</th>
              <th className="cell">Marks</th>
              <th className="cell">Set</th>
            </tr>
          </thead>
          <tbody>
            {students.map(s => (
              <tr key={s.id} className="row">
                <td className="cell"><input type="checkbox" checked={!!selected[s.id]} onChange={() => toggleSelect(s.id)} /></td>
                <td className="cell">{s.rollNumber}</td>
                <td className="cell">{s.firstName} {s.lastName}</td>
                <td className="cell">{s.marks ?? '-'}</td>
                <td className="cell">
                  <input className="input-small" type="number" step="0.1" value={grades[s.id] ?? (s.marks ?? '')} onChange={e => onChange(s.id, e.target.value)} />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="mt-4">
        <button className="btn btn-success" onClick={submitGrades}>Save Grades</button>
      </div>
    </div>
  )
}
