"use client";

import type React from "react";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { signIn } from "next-auth/react";
import { Eye, EyeOff, Lock, User, AlertCircle } from "lucide-react";
import Image from "next/image";
import { motion } from "motion/react";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    setError(null);

    if (!username.trim()) {
      setError("Username is required");
      return;
    }

    if (!password) {
      setError("Password is required");
      return;
    }

    setIsLoading(true);

    try {
      const result = await signIn("credentials", {
        username,
        password,
        redirect: false,
      });

      if (result?.error) {
        setError("Invalid username or password");
      } else {
        router.push("/dashboard");
      }
    } catch {
      setError("An error occurred. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  // Animation variants
  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1,
        delayChildren: 0.2,
      },
    },
  };

  const itemVariants = {
    hidden: { y: 20, opacity: 0 },
    visible: {
      y: 0,
      opacity: 1,
      transition: {
        type: "spring",
        stiffness: 100,
        damping: 15,
      },
    },
  };

  const buttonVariants = {
    hidden: { scale: 0.95, opacity: 0 },
    visible: {
      scale: 1,
      opacity: 1,
      transition: {
        type: "spring",
        stiffness: 200,
        damping: 15,
      },
    },
    hover: {
      scale: 1.03,
      boxShadow:
        "0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)",
      transition: {
        type: "spring",
        stiffness: 400,
        damping: 10,
      },
    },
    tap: { scale: 0.98 },
  };

  return (
    <div className="h-screen w-full flex items-center justify-center bg-gray-100 dark:bg-gray-950">
      <div className="w-full max-w-6xl mx-auto grid grid-cols-1 lg:grid-cols-2 rounded-3xl overflow-hidden shadow-lg">
        {/* Left Panel - Login Form */}
        <motion.div
          className="bg-white dark:bg-gray-900 p-8 md:p-10 flex flex-col justify-center"
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.5 }}
        >
          <motion.div
            className="mb-6"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.2 }}
          >
            <h2 className="text-3xl font-black text-cyan-800 dark:text-gray-200">
              NoPolin HUB
            </h2>
          </motion.div>

          <motion.div
            className="mb-8"
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.3 }}
          >
            <p className="text-gray-600 dark:text-gray-400 text-sm">
              Your centralized solution to reduce queues and optimize transport
              across Sri Lanka.
            </p>
          </motion.div>

          {/* Error message display */}
          {error && (
            <motion.div
              className="mb-4 p-3 bg-pink-50 dark:bg-pink-900/20 border border-pink-200 dark:border-pink-800 rounded-lg flex items-start gap-2 text-pink-700 dark:text-pink-300"
              initial={{ opacity: 0, y: -10 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ type: "spring", stiffness: 500, damping: 30 }}
            >
              <AlertCircle className="h-5 w-5 flex-shrink-0 mt-0.5" />
              <div>
                <p className="text-sm font-medium">{error}</p>
                <p className="text-xs mt-1">
                  Please check your credentials and try again.
                </p>
              </div>
            </motion.div>
          )}

          <motion.form
            onSubmit={handleSubmit}
            className="space-y-5"
            variants={containerVariants}
            initial="hidden"
            animate="visible"
          >
            <motion.div className="space-y-2" variants={itemVariants}>
              <Label
                htmlFor="username"
                className="text-sm font-medium text-gray-700 dark:text-gray-300"
              >
                Username
              </Label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-gray-400 dark:text-gray-500">
                  <User className="h-5 w-5" />
                </div>
                <Input
                  id="username"
                  type="text"
                  placeholder="Enter your username"
                  required
                  value={username}
                  onChange={(e) => {
                    setUsername(e.target.value);
                    if (error) setError(null);
                  }}
                  className={`pl-10 h-12 bg-white dark:bg-gray-800 border ${
                    error && !username.trim()
                      ? "border-pink-500 dark:border-pink-700 focus-visible:ring-pink-500"
                      : "border-gray-200 dark:border-gray-700 focus-visible:ring-zinc-500"
                  } text-gray-900 dark:text-white rounded-xl focus-visible:ring-2 focus-visible:ring-offset-0`}
                  aria-invalid={error && !username.trim() ? "true" : "false"}
                />
              </div>
            </motion.div>

            <motion.div className="space-y-2" variants={itemVariants}>
              <Label
                htmlFor="password"
                className="text-sm font-medium text-gray-700 dark:text-gray-300"
              >
                Password
              </Label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-gray-400 dark:text-gray-500">
                  <Lock className="h-5 w-5" />
                </div>
                <Input
                  id="password"
                  type={showPassword ? "text" : "password"}
                  required
                  placeholder="••••••••"
                  value={password}
                  onChange={(e) => {
                    setPassword(e.target.value);
                    if (error) setError(null);
                  }}
                  className={`pl-10 pr-10 h-12 bg-white dark:bg-gray-800 border ${
                    error && !password
                      ? "border-pink-500 dark:border-pink-700 focus-visible:ring-pink-500"
                      : "border-gray-200 dark:border-gray-700 focus-visible:ring-zinc-500"
                  } text-gray-900 dark:text-white rounded-xl focus-visible:ring-2 focus-visible:ring-offset-0`}
                  aria-invalid={error && !password ? "true" : "false"}
                />
                <button
                  type="button"
                  onClick={togglePasswordVisibility}
                  className="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-400 dark:text-gray-500"
                >
                  {showPassword ? (
                    <EyeOff className="h-5 w-5" />
                  ) : (
                    <Eye className="h-5 w-5" />
                  )}
                </button>
              </div>
            </motion.div>

            <motion.div
              className="flex items-center justify-between"
              variants={itemVariants}
            >
              <div className="flex items-center">
                <input
                  id="remember-me"
                  name="remember-me"
                  type="checkbox"
                  className="h-4 w-4 text-zinc-600 focus:ring-zinc-500 border-gray-300 rounded"
                />
                <label
                  htmlFor="remember-me"
                  className="ml-2 block text-sm text-gray-700 dark:text-gray-300"
                >
                  Remember me
                </label>
              </div>

              <div className="text-sm">
                <a
                  href="/forget-password"
                  className="font-medium text-zinc-600 hover:text-zinc-500"
                >
                  Forgot password?
                </a>
              </div>
            </motion.div>

            <motion.div variants={itemVariants}>
              <motion.button
                type="submit"
                disabled={isLoading}
                className="w-full h-12 bg-zinc-700 hover:bg-zinc-800 text-white dark:bg-zinc-600 dark:hover:bg-zinc-700 rounded-xl shadow-lg hover:shadow-zinc-500/20 transition-all duration-200"
                variants={buttonVariants}
                whileHover="hover"
                whileTap="tap"
              >
                {isLoading ? (
                  <div className="flex items-center justify-center">
                    <div className="w-5 h-5 border-2 border-t-transparent border-white rounded-full animate-spin mr-2"></div>
                    Logging in...
                  </div>
                ) : (
                  <div className="flex items-center justify-center">Login</div>
                )}
              </motion.button>
            </motion.div>
          </motion.form>

          <motion.div
            className="mt-8 pt-6 border-t border-gray-200 dark:border-gray-800"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.8 }}
          >
            <div className="flex items-center space-x-3">
              <motion.div
                className="flex -space-x-2"
                initial={{ x: -20, opacity: 0 }}
                animate={{ x: 0, opacity: 1 }}
                transition={{ delay: 0.9, type: "spring", stiffness: 200 }}
              >
                {[1, 2, 3].map((i) => (
                  <motion.div
                    key={i}
                    className="w-8 h-8 rounded-full bg-gradient-to-br from-zinc-500 to-zinc-700 border-2 border-white dark:border-gray-900 flex items-center justify-center overflow-hidden"
                    initial={{ scale: 0, opacity: 0 }}
                    animate={{ scale: 1, opacity: 1 }}
                    transition={{ delay: 0.9 + i * 0.1, type: "spring" }}
                  >
                    <span className="text-xs text-white font-bold">{i}</span>
                  </motion.div>
                ))}
              </motion.div>
              <motion.div
                className="text-sm text-gray-600 dark:text-gray-400"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ delay: 1.2 }}
              >
                <p className="font-medium">Secure Private Access</p>
                <p className="text-xs">Enterprise security system</p>
              </motion.div>
            </div>
          </motion.div>
        </motion.div>

        <motion.div
          className="relative hidden lg:block"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ duration: 0.8 }}
        >
          <div className="absolute inset-0 bg-gradient-to-r from-zinc-800/20 to-zinc-700/30 z-10"></div>
          {/* Background image */}
          <Image
            src="/login/sri-lanka.png"
            alt="Login background"
            fill
            className="object-cover object-center z-0"
            priority
          />

          {/* Foreground content */}
          <div className="absolute inset-0 flex flex-col justify-between z-10 p-10">
            <motion.div
              className="space-y-4"
              initial={{ opacity: 0, y: -20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.5, duration: 0.8 }}
            >
              <h2 className="text-2xl font-bold text-white drop-shadow-lg">
                Smarter Transport, Fewer Queues
              </h2>
            </motion.div>

            <motion.div
              className="space-y-4"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.7, duration: 0.8 }}
            >
              <motion.div
                className="flex"
                initial={{ scale: 0.9, opacity: 0 }}
                animate={{ scale: 1, opacity: 1 }}
                transition={{ delay: 0.9, type: "spring" }}
              >
                <div className="inline-flex items-center px-6 py-2 rounded-full bg-white/20 backdrop-blur-sm text-white border border-white/30">
                  <span className="text-sm font-medium">
                    Centralized Control
                  </span>
                </div>
              </motion.div>

              <p className="text-white text-sm drop-shadow-md">
                Track vehicles, manage lines, and improve efficiency across Sri
                Lanka — all in one unified platform.
              </p>
            </motion.div>
          </div>
        </motion.div>
      </div>
    </div>
  );
}
