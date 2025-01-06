const { createApp } = Vue

createApp({
    data() {
        return {
            admins: [],
            users: []
        }
    },
    methods: {
        async loadData() {
            try {
                const adminsResponse = await axios.get('/api/dashboard/admins')
                this.admins = adminsResponse.data

                const usersResponse = await axios.get('/api/dashboard/users')
                this.users = usersResponse.data
            } catch (error) {
                alert('Erro ao carregar dados')
            }
        },
        async removerAdmin(adminId) {
            if (confirm('Tem certeza que deseja remover este admin?')) {
                try {
                    await axios.delete(`/api/dashboard/admin/${adminId}`)
                    this.loadData()
                } catch (error) {
                    alert('Erro ao remover admin')
                }
            }
        },
        async removerUsuario(userId) {
            if (confirm('Tem certeza que deseja remover este usuário?')) {
                try {
                    await axios.delete(`/api/dashboard/user/${userId}`)
                    this.loadData()
                } catch (error) {
                    alert('Erro ao remover usuário')
                }
            }
        }
    },
    mounted() {
        this.loadData()
    }
}).mount('#app')