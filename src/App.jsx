import './App.css'
import { Link, Routes, Route } from 'react-router-dom'
import { Home, About, Login, Courses, CourseStudents } from '@pages'
import Navbar from './components/Navbar'

function App() {
  return (
    <div className="app-layout">
      <Navbar />
      <main className="main-content">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<About />} />
          <Route path="/login" element={<Login />} />
          <Route path="/employee/:id/courses" element={<Courses />} />
          <Route path="/courses/:id/students" element={<CourseStudents />} />
        </Routes>
      </main>
    </div>
  )
}

export default App
