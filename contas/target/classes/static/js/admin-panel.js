const { createApp } = Vue

createApp({
    data() {
        return {
            users: [],
            novoUsuario: {
                nome: '',
                email: '',
                documento: '',
                senha: ''
            }
        }
    },
    methods: {
        async loadUsers() {
            try {
                const response = await axios.get('/api/admin/users')
                this.users = response.data
            } catch (error) {
                alert('Erro ao carregar usuários')
            }
        },
        async adicionarUsuario() {
            try {
                await axios.post('/api/admin/users', this.novoUsuario)
                this.novoUsuario = { nome: '', email: '', documento: '', senha: '' }
                this.loadUsers()
                alert('Usuário adicionado com sucesso!')
            } catch (error) {
                alert('Erro ao adicionar usuário')
            }
        },
        async removerUsuario(userId) {
            if (confirm('Tem certeza que deseja remover este usuário?')) {
                try {
                    await axios.delete(`/api/admin/users/${userId}`)
                    this.loadUsers()
                    alert('Usuário removido com sucesso!')
                } catch (error) {
                    alert('Erro ao remover usuário')
                }
            }
        }
    },
    mounted() {
        this.loadUsers()
    }
}).mount('#app')