# Report 2: Course Enrollment

### Han Raby
- Create a AuthController.java
- Create a SecurityConfig.java
- Create a UserDetailsServiceImpl.java
- Create a RoleRepository.java & UserRepository.java
- Create a DashboardController.java for testing

### Hun Lyhorng
-  Designing UI in Figma
- Create LoginForm.html using html, tailwindcss, javascript, thymeleaf

### Heng oulong
- Create a CourseRepository.java
- Create dto: CourseCreateUpdateDto.java and CourseDto.java
- Create a service: CourseService.java
- Create exception: resourceNotFoundException
- Create GlobalExceptionHandler.java
- Create controllers: CourseController.java

### Chea Chanminea
1. Integrate Database Design in coding

2. Description of Tables
- users: Stores user details (username, password_hash, email, name, DOB, role, status).
- roles: Defines user roles (ADMIN, LECTURER, STUDENT).
- courses: Course information (code, name, credits, max_capacity, lecturer, status).
- classrooms: Room details (building, room_number, capacity, equipment).
- schedules: Class timetables (course, classroom, day, time, semester, year).
- enrollments: Student-course registrations (status, grade, score, attendance).

3. Relationships
- One-to-Many: User → Courses (lecturer), User → Enrollments (student), Course → Schedules/Enrollments.
- Many-to-One: User → Role, Course → User (lecturer), Schedule → Course/Classroom.
- Many-to-Many: Student ↔️ Course via enrollments table.

4. Constraints & Indexes
- Unique constraints on username, email, course_code.
- Foreign keys with proper naming.
- Indexes on frequently queried fields (status, lecturer_id, etc.).
