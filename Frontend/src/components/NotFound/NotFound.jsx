import './NotFound.css';

function NotFound() {
  return (
    <div className="notfound-container">
      <h1 className="notfound-title">404</h1>
      <p className="notfound-message">Página no encontrada</p>
      <a href="/home" className="notfound-link">Volver al inicio</a>
    </div>
  );
}

export default NotFound;
