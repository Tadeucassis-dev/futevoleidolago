// Exemplo de como o frontend deveria tratar as respostas da API

// CADASTRO DE USUÁRIO (Admin)
async function handleRegister(userData) {
    try {
        const response = await fetch('http://localhost:8080/api/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
        });

        const data = await response.json();

        if (data.success) {
            // Sucesso - redirecionar para login
            alert(data.message); // "Usuário cadastrado com sucesso! Você pode fazer login agora."
            window.location.href = '/login'; // ou usar navigate('/login') no React Router
        } else {
            // Erro - mostrar mensagem e permanecer na página
            alert('Erro: ' + data.error);
        }
    } catch (error) {
        console.error('Erro na requisição:', error);
        alert('Erro de conexão com o servidor');
    }
}

// LOGIN
async function handleLogin(credentials) {
    try {
        const response = await fetch('http://localhost:8080/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(credentials)
        });

        const data = await response.json();

        if (data.success) {
            // Sucesso - salvar token e redirecionar para home
            localStorage.setItem('token', data.token);
            alert(data.message); // "Login realizado com sucesso"
            window.location.href = '/home'; // ou usar navigate('/home') no React Router
        } else {
            // Erro - mostrar mensagem e permanecer na página
            alert('Erro: ' + data.error);
        }
    } catch (error) {
        console.error('Erro na requisição:', error);
        alert('Erro de conexão com o servidor');
    }
}

// CADASTRO DE ALUNO
async function handleAlunoRegister(alunoData) {
    try {
        const response = await fetch('http://localhost:8080/api/alunos/cadastrar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(alunoData)
        });

        const data = await response.json();

        if (data.success) {
            // Sucesso - mostrar mensagem e redirecionar
            alert(data.message); // "Solicitação de cadastro enviada com sucesso! Aguarde a aprovação."
            window.location.href = '/login'; // ou para onde desejar
        } else {
            // Erro - mostrar mensagem e permanecer na página
            alert('Erro: ' + data.error);
        }
    } catch (error) {
        console.error('Erro na requisição:', error);
        alert('Erro de conexão com o servidor');
    }
}

// Exemplo usando React Router (se estiver usando React)
/*
import { useNavigate } from 'react-router-dom';

function LoginComponent() {
    const navigate = useNavigate();

    const handleLogin = async (credentials) => {
        try {
            const response = await fetch('http://localhost:8080/api/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(credentials)
            });

            const data = await response.json();

            if (data.success) {
                localStorage.setItem('token', data.token);
                navigate('/home'); // Redireciona para home
            } else {
                setError(data.error); // Mostra erro na tela
            }
        } catch (error) {
            setError('Erro de conexão');
        }
    };
}
*/