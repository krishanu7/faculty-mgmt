import React, { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { getCourses } from '../api'

export default function Courses(){
  const { id } = useParams()
  const employeeId = id || localStorage.getItem('employeeId')
  const [courses, setCourses] = useState([])
  const [error, setError] = useState(null)

  useEffect(()=>{
    if(!employeeId) return
    getCourses(employeeId).then(setCourses).catch(e => setError(e.message))
  },[employeeId])

  if(error) return <div className="max-w-md mx-auto mt-8 p-4 bg-white rounded shadow"><p className="text-red-600">{error}</p></div>

  return (
    <div className="courses-card">
      <h2 className="courses-title">Courses taught by {employeeId}</h2>
      <ul className="courses-list">
        {courses.map(c => (
          <li key={c.id} className="course-item">
            <div>
              <div className="course-name">{c.code} - {c.name}</div>
            </div>
            <div>
              <Link className="link" to={`/courses/${c.id}/students`}>View students</Link>
            </div>
          </li>
        ))}
      </ul>
      {courses.length === 0 && <p className="muted">No courses found</p>}
    </div>
  )
}
