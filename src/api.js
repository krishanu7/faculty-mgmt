const BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080/api'

export async function loginEmployee(employeeId){
  const res = await fetch(`${BASE}/employee/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ employeeId })
  })
  if(!res.ok) throw new Error(await res.text())
  return res.json()
}

export async function getCourses(employeeId){
  const res = await fetch(`${BASE}/employee/${employeeId}/courses`)
  if(!res.ok) throw new Error(await res.text())
  return res.json()
}

export async function getStudents(courseId){
  const res = await fetch(`${BASE}/courses/${courseId}/students`)
  if(!res.ok) throw new Error(await res.text())
  return res.json()
}

export async function postGrades(courseId, employeeId, grades){
  const res = await fetch(`${BASE}/courses/${courseId}/grades`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ employeeId, grades })
  })
  if(!res.ok) throw new Error(await res.text())
  return res.json()
}

export default { loginEmployee, getCourses, getStudents, postGrades }
