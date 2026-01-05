// import { Box, TextField, Button } from "@mui/material";
// import { useState } from "react";
// import TodoItem from "./TodoItem";
// import { createTodo, updateTodo } from "../../api/boardAPI";

// export default function TodoList({ boardId, todos, refreshTodos }) {
//   const [newTodo, setNewTodo] = useState("");

//   const handleAdd = async () => {
//     if (!newTodo.trim()) return;
//     await createTodo(boardId, newTodo);
//     setNewTodo("");
//     refreshTodos();
//   };

//   const handleToggle = async (id) => {
//     const todo = todos.find((t) => t.id === id);
//     await updateTodo(boardId, id, { completed: !todo.completed });
//     refreshTodos();
//   };

//   const handleDelete = async (id) => {
//     await updateTodo(boardId, id, { deleted: true }); 
//     refreshTodos();
//   };

//   return (
//     <Box>
//       <Box sx={{ display: "flex", mb: 2 }}>
//         <TextField
//           label="New Todo"
//           value={newTodo}
//           onChange={(e) => setNewTodo(e.target.value)}
//           fullWidth
//         />
//         <Button sx={{ ml: 2 }} variant="contained" onClick={handleAdd}>
//           Add
//         </Button>
//       </Box>

//       {todos.map((todo) => (
//         <TodoItem
//           key={todo.id}
//           todo={todo}
//           onToggle={handleToggle}
//           onDelete={handleDelete}
//         />
//       ))}
//     </Box>
//   );
// }
