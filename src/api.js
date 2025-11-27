const BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080/api'

// Helper function to get JWT token from localStorage
function getAuthToken() {
  return localStorage.getItem('jwtToken')
}

// Helper function to create headers with JWT token
function createHeaders(includeAuth = true) {
  const headers = {
    'Content-Type': 'application/json'
  }
  
  if (includeAuth) {
    const token = getAuthToken()
    if (token) {
      headers['Authorization'] = `Bearer ${token}`
    }
  }
  
  return headers
}

// Helper function to handle responses
async function handleResponse(res) {
  if (res.status === 401) {
    // Unauthorized - clear token and redirect to login
    localStorage.removeItem('jwtToken')
    localStorage.removeItem('employeeId')
    window.location.href = '/login'
    throw new Error('Unauthorized - please login again')
  }
  
  if (!res.ok) {
    throw new Error(await res.text())
  }
  
  return res.json()
}

export async function loginEmployee(email, password, googleToken) {
  const body = googleToken ? { googleToken } : { email, password }
  const res = await fetch(`${BASE}/employee/login`, {
    method: 'POST',
    headers: createHeaders(false), // Don't include auth for login
    body: JSON.stringify(body)
  })
  
  const data = await handleResponse(res)
  
  // Store JWT token and employee ID
  if (data.token) {
    localStorage.setItem('jwtToken', data.token)
    localStorage.setItem('employeeId', data.id)
  }
  
  return data
}

export async function getCourses(employeeId) {
  const res = await fetch(`${BASE}/employee/${employeeId}/courses`, {
    headers: createHeaders()
  })
  return handleResponse(res)
}

export async function getStudents(courseId) {
  const res = await fetch(`${BASE}/courses/${courseId}/students`, {
    headers: createHeaders()
  })
  return handleResponse(res)
}

export async function postGrades(courseId, employeeId, grades) {
  const res = await fetch(`${BASE}/courses/${courseId}/grades`, {
    method: 'POST',
    headers: createHeaders(),
    body: JSON.stringify({ employeeId, grades })
  })
  return handleResponse(res)
}

export function logout() {
  localStorage.removeItem('jwtToken')
  localStorage.removeItem('employeeId')
}

export default { loginEmployee, getCourses, getStudents, postGrades, logout }
