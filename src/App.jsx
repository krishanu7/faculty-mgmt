import './App.css'
import { Link, Routes, Route } from 'react-router-dom'
import { Home, About, Login, Courses, CourseStudents } from '@pages'

function App() {
  return (
    <>
      <header className="app-header">
        <h1>Faculty Frontend</h1>
        <nav>
          <Link to="/">Home</Link>
          <Link to="/about">About</Link>
          <Link to="/login">Login</Link>
        </nav>
      </header>

      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<About />} />
          <Route path="/login" element={<Login />} />
          <Route path="/employee/:id/courses" element={<Courses />} />
          <Route path="/courses/:id/students" element={<CourseStudents />} />
        </Routes>
      </main>
    </>
  )
}

export default App
