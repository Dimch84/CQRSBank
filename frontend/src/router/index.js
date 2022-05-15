import { createWebHistory, createRouter } from "vue-router";
import HelloWorld from '@/components/HelloWorld'
import GreetingComponent from '@/components/GreetingComponent'

const routes = [
  {
    path: "/hello-world",
    name: "HelloWorld",
    component: HelloWorld,
  },
  {
    path: "/greeting",
    name: "GreetingComponent",
    component: GreetingComponent,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;